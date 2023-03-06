package me.silloy.expression.utils;

import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ClassLoaderUtils {
    private static final String RESOURCE_PATTERN = "/**/*.class";


    @SafeVarargs
    public static List<TypeFilter> annotationTypeFilters(Class<? extends Annotation>... annotationFilter) {
        List<TypeFilter> typeFilters = new LinkedList<>();
        if (annotationFilter != null) {
            for (Class<? extends Annotation> annotation : annotationFilter) {
                typeFilters.add(new AnnotationTypeFilter(annotation, false));
            }
        }
        return typeFilters;
    }

    @SafeVarargs
    public static Set<Class<?>> loadPackageClasses(String packages,
                                                   Class<? extends Annotation>... annotationFilter) throws IOException, ClassNotFoundException {
        return loadPackageClasses(Sets.newHashSet(packages), annotationFilter);
    }

    @SafeVarargs
    public static Set<Class<?>> loadPackageClasses(Set<String> packagesList,
                                                   Class<? extends Annotation>... annotationFilter) throws IOException, ClassNotFoundException {
        Set<Class<?>> classSet = new HashSet<>();
        if (CollectionUtils.isEmpty(packagesList)) {
            return classSet;
        }
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        for (String pkg : packagesList) {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    List<TypeFilter> typeFilters = annotationTypeFilters(annotationFilter);
                    if (matchesEntityTypeFilter(typeFilters, reader, readerFactory)) {
                        Class clazz = Class.forName(className);
                        classSet.add(clazz);
                    }
                }
            }
        }
        return classSet;
    }


    private static boolean matchesEntityTypeFilter(List<TypeFilter> typeFilters, MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
        if (!typeFilters.isEmpty()) {
            for (TypeFilter filter : typeFilters) {
                if (filter.match(reader, readerFactory)) {
                    return true;
                }
            }
        }
        return false;
    }


}
