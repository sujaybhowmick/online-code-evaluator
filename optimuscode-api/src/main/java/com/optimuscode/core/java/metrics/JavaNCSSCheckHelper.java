package com.optimuscode.core.java.metrics;

import com.optimuscode.core.common.model.Project;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.checks.metrics.JavaNCSSCheck;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/11/13
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class JavaNCSSCheckHelper implements MetricCheckHelper {

    public JavaNCSSCheckHelper(){
    }

    @Override
    public void generateMetricsForProject(final Project project) {
        final String srcDir = project.getSourceFolder();
        final String sourceFile = project.getClassName();
        final String fileName = srcDir + File.separatorChar + sourceFile;
        DefaultConfiguration checkConfig =
                                    createCheckConfig(JavaNCSSCheck.class);
        checkConfig.addAttribute("methodMaximum", "0");
        checkConfig.addAttribute("classMaximum", "1");
        checkConfig.addAttribute("fileMaximum", "2");
        try {
            verify(checkConfig, fileName);
        }catch(Exception e) {
            throw new RuntimeException(e);

        }

    }

    protected static DefaultConfiguration createCheckConfig(Class<?> aClazz){
        final DefaultConfiguration checkConfig =
                new DefaultConfiguration(aClazz.getName());
        return checkConfig;
    }

    protected void verify(Configuration aConfig, String aFileName)
                                                        throws Exception{
        verify(createChecker(aConfig), aFileName);
    }

    protected void verify(Checker aC, String aFileName) throws Exception{
        verify(aC, new File[] {new File(aFileName)});
    }

    protected void verify(Checker aC, File[] processedFiles) throws Exception{
        try{
            final List<File> theFiles = Lists.newArrayList();
            Collections.addAll(theFiles, processedFiles);
            final int errors = aC.process(theFiles);
        }finally{
            aC.destroy();
        }
    }


    protected Checker createChecker(Configuration aCheckConfig)
            throws Exception{
        final DefaultConfiguration dc = createCheckerConfig(aCheckConfig);
        final Checker c = new Checker();
        // make sure the tests always run with english error messages
        // so the tests don't fail in supported locales like german
        final Locale locale = Locale.ENGLISH;
        c.setLocaleCountry(locale.getCountry());
        c.setLocaleLanguage(locale.getLanguage());
        c.setModuleClassLoader(Thread.currentThread().getContextClassLoader());
        c.configure(dc);
        c.addListener(new JavaNCSSAuditListener());
        return c;
    }

    protected DefaultConfiguration createCheckerConfig(Configuration aConfig){
        final DefaultConfiguration dc =
                new DefaultConfiguration("configuration");
        final DefaultConfiguration twConf = createCheckConfig(TreeWalker.class);
        // make sure that the tests always run with this charset
        dc.addAttribute("charset", "iso-8859-1");
        dc.addChild(twConf);
        twConf.addChild(aConfig);
        return dc;
    }

    protected class JavaNCSSAuditListener extends AbstractAuditListener {
        @Override
        public void addError(AuditEvent auditEvent) {
            System.out.println(auditEvent.getFileName() + ":" +
                                auditEvent.getMessage());
        }

        @Override
        public void addException(AuditEvent auditEvent, Throwable throwable) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
