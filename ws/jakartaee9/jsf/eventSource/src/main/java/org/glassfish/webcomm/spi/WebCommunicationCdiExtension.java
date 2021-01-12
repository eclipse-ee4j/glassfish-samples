/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.webcomm.spi;

import org.glassfish.webcomm.annotation.WebHandler;
import org.glassfish.webcomm.annotation.WebHandlerContext;
import org.glassfish.webcomm.annotation.WebMessageProcessor;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebSocketHandler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.*;
import jakarta.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebCommunicationExtension class
 * @author Santiago.PericasGeertsen@oracle.com
 * @author Jitendra Kotamraju
 */
public class WebCommunicationCdiExtension implements Extension {

    private WebCommunicationProvider provider;

    private final Map<String, WebCommunicationContext> contexts
        = new ConcurrentHashMap<String, WebCommunicationContext>();

    public WebCommunicationCdiExtension() {
        ServiceLoader<WebCommunicationProvider> wcp =
                ServiceLoader.load(WebCommunicationProvider.class);
        Iterator<WebCommunicationProvider> it = wcp.iterator();
        assert it.hasNext();
        provider = it.next();
    }

    public WebCommunicationProvider getProvider() {
        return provider;
    }

    public Map<String, WebCommunicationContext> getContexts() {
        return contexts;
    }

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {
        provider.setBeanManager(bm);
        bbd.addQualifier(WebHandlerContext.class);
    }

    static class WebHandlerContextAnnotationLiteral extends AnnotationLiteral<WebHandlerContext> implements WebHandlerContext {
        private final String path;

        WebHandlerContextAnnotationLiteral(String path) {
            this.path = path;
        }

        @Override
        public String value() {
            return path;
        }
    }

    static class WebCommunicationContextBean implements Bean<WebCommunicationContext> {

        private final String path;
        private final WebCommunicationContext instance;

        WebCommunicationContextBean(String path, WebCommunicationContext instance) {
            this.path = path;
            this.instance = instance;
        }

        @Override
        public Set<Type> getTypes() {
            Set<Type> types = new HashSet<Type>();
            types.add(WebCommunicationContext.class);
            return types;
        }

        @Override
        public Set<Annotation> getQualifiers() {
            Set<Annotation> qualifiers = new HashSet<Annotation>();
            qualifiers.add(new WebHandlerContextAnnotationLiteral(path));
            return qualifiers;
        }

        @Override
        public Class<? extends Annotation> getScope() {
            return ApplicationScoped.class;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Set<Class<? extends Annotation>> getStereotypes() {
            return Collections.emptySet();
        }

        @Override
        public Class<?> getBeanClass() {
            return WebCommunicationContext.class;
        }

        @Override
        public boolean isAlternative() {
            return false;
        }

        @Override
        public boolean isNullable() {
            return false;
        }

        @Override
        public Set<InjectionPoint> getInjectionPoints() {
            return Collections.emptySet();
        }

        @Override
        public WebCommunicationContext create(CreationalContext<WebCommunicationContext> context) {
            return instance;
        }

        @Override
        public void destroy(WebCommunicationContext instance, CreationalContext<WebCommunicationContext> context) {
        }
    }

    // For each WebHandler, it creates a corresponding WebCommunicationContext
    // This WebCommunicationContext can be got anywhere from BeanManager
    void afterBeanDiscovery(@Observes AfterBeanDiscovery bbd) {
        for(Map.Entry<String, WebCommunicationContext> entry : contexts.entrySet()) {
            bbd.addBean(new WebCommunicationContextBean(entry.getKey(), entry.getValue()));
        }
    }

    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat,
            BeanManager beanManager) {
        System.out.println("scanning type: " + pat.getAnnotatedType().getJavaClass().getName());

        for (Annotation an : pat.getAnnotatedType().getAnnotations()) {
            Class clazz = pat.getAnnotatedType().getJavaClass();

            if (an instanceof WebHandler) {
                WebHandler wh = (WebHandler) an;
                String path = normalizePath(wh.value());

                // Get or create communication context for this path
                WebCommunicationContext wcc = contexts.get(path);
                if (wcc == null) {
                    wcc = provider.createWebCommunicationContext(path);
                    contexts.put(path, wcc);
                }

                if (WebSocketHandler.class.isAssignableFrom(clazz)) {
                    provider.registerWSHandlerClass(clazz, contexts, path);
                } else if (ServerSentEventHandler.class.isAssignableFrom(clazz)) {
                    provider.registerSSEHandlerClass(clazz, contexts, path);
                } else {
                    throw new RuntimeException("Invalid base class '"
                            + clazz.getName() + "' for handler");
                }
            } else if (an instanceof WebMessageProcessor) {
                WebMessageProcessor wmp = (WebMessageProcessor) an;
                provider.registerMessageProcessor(wmp.value(), clazz);
            }
        }
    }

    private String normalizePath(String path) {
        path = path.trim();
        return path.startsWith("/") ? path : "/" + path;
    }

}
