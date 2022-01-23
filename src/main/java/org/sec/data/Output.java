package org.sec.data;

import org.sec.log.SLF4J;
import org.sec.model.CallGraph;
import org.sec.model.MethodReference;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SLF4J
public class Output {
    
    private static Logger logger;

    public static void writeTargetCallGraphs(Map<MethodReference.Handle, Set<CallGraph>> graphCallMap,
                                             String packageName) {
        logger.info("write call graphs data");
        if (packageName == null || packageName.equals("")) {
            logger.error("need package name config");
            return;
        }
        packageName = packageName.replace(".", "/");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<MethodReference.Handle, Set<CallGraph>> item : graphCallMap.entrySet()) {
            if (item.getKey().getClassReference().getName().startsWith(packageName)) {
                for (CallGraph callGraph : item.getValue()) {
                    sb.append(callGraph.getCallerMethod().getClassReference().getName());
                    sb.append(".");
                    sb.append(callGraph.getCallerMethod().getName());
                    sb.append("#");
                    sb.append(callGraph.getCallerArgIndex());
                    sb.append("--->");
                    sb.append(callGraph.getTargetMethod().getClassReference().getName());
                    sb.append(".");
                    sb.append(callGraph.getTargetMethod().getName());
                    sb.append("#");
                    sb.append(callGraph.getTargetArgIndex());
                    sb.append("\n");
                }
            }
        }
        try {
            Files.write(Paths.get("calls.txt"), sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeSortedMethod(List<MethodReference.Handle> sortedMethods) {
        logger.info("write sorted method data");
        StringBuilder sb = new StringBuilder();
        for (MethodReference.Handle method : sortedMethods) {
            sb.append(method.getClassReference().getName());
            sb.append(".");
            sb.append(method.getName());
            sb.append("\n");
        }
        try {
            Files.write(Paths.get("sorted.txt"), sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}