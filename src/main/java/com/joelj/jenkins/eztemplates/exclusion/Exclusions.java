package com.joelj.jenkins.eztemplates.exclusion;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jenkins.model.Jenkins;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Exclusions {

    public static final Map<String, Exclusion> ALL;
    public static final List<String> DEFAULT;

    public static final String MATRIX_SECURITY_ID = "matrix-auth";
    public static final String OWNERSHIP_ID = "ownership";
    public static final String GITHUB_ID = "github";

    static {
        ImmutableList.Builder<Exclusion> builder = ImmutableList.builder();
        builder.add(new EzTemplatesExclusion());
		builder.add(new DiscardExclusion());
        builder.add(new JobParametersExclusion());
        builder.add(new TriggersExclusion());
        builder.add(new DisabledExclusion());
        builder.add(new DescriptionExclusion());
        builder.add(new PromotedBuildsExclusion());
        builder.add(new OwnershipExclusion());
        builder.add(new JobPropertyExclusion(MATRIX_SECURITY_ID, "Retain local matrix-build security", "hudson.security.AuthorizationMatrixProperty"));
        builder.add(new JobPropertyExclusion(GITHUB_ID, "Retain local Github details", "com.coravy.hudson.plugins.github.GithubProjectProperty"));
        builder.add(new ScmExclusion());
        builder.add(new AssignedLabelExclusion());
        builder.add(new MatrixAxisExclusion());
        List<Exclusion> l = builder.build();
        ALL = Maps.uniqueIndex(l, new Function<Exclusion, String>() {
            @Override
            public String apply(@Nonnull Exclusion exclusion) {
                return exclusion.getId();
            }
        });
    }

    static {
        DEFAULT = ImmutableList.of(
                EzTemplatesExclusion.ID,
                JobParametersExclusion.ID,
                DisabledExclusion.ID,
                DescriptionExclusion.ID
        );
    }

    /**
     * {@link Exclusion}s currently enabled in Jenkins.
     *
     * @return Never null
     */
    public static Collection<Exclusion> enabledExceptions() {
        // TODO Stop ez-templates being so special!
        return Collections2.filter(ALL.values(), new Predicate<Exclusion>() {
            @Override
            public boolean apply(@Nonnull Exclusion input) {
                return EzTemplatesExclusion.ID.equals(input.getId()) || input.getDisabledText() == null;
            }
        });
    }

    @SuppressFBWarnings
    public static String checkPlugin(String id) {
        return Jenkins.getInstance().getPlugin(id) == null ? String.format("Plugin %s is not installed", id) : null;
    }

}
