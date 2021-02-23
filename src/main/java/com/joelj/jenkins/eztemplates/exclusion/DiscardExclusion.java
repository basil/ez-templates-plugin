package com.joelj.jenkins.eztemplates.exclusion;

import com.google.common.base.Throwables;
import hudson.model.AbstractProject;
import hudson.model.Label;
import jenkins.model.BuildDiscarder;

import java.io.IOException;

public class DiscardExclusion extends AbstractExclusion {

    public static final String ID = "discard-label";
    private static final String DESCRIPTION = "Retain Discard Strategy";

    public DiscardExclusion() {
        super(ID, DESCRIPTION);
    }

    @Override
    public String getDisabledText() {
        return null;
    }

    @Override
    public void preClone(EzContext context, AbstractProject implementationProject) {
        if (!context.isSelected()) return;
        context.record(implementationProject.getBuildDiscarder());
    }

    @Override
    public void postClone(EzContext context, AbstractProject implementationProject) {
        if (!context.isSelected()) return;
        BuildDiscarder rot = context.remember();
        try {
            implementationProject.setBuildDiscarder(rot);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

}
