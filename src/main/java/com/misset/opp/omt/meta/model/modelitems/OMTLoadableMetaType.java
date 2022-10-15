package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.scalars.values.OMTLoadablePathMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTLoadableSchemaMetaType;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;
import java.util.function.Supplier;

public class OMTLoadableMetaType extends OMTModelItemDelegateMetaType implements OMTMetaCallable, OMTDocumented {

    private static final OMTLoadableMetaType INSTANCE = new OMTLoadableMetaType();

    public static OMTLoadableMetaType getInstance() {
        return INSTANCE;
    }

    protected static final String RELEASE_FLAG_SHOULD_NOT_BE_COMBINED_WITH_OTHER_FLAGS = "!release flag should not be combined with other flags";
    protected static final String CALLING_WITH_RETAIN_OR_RELEASE_FLAG = "Context argument only expected when calling with !retain or !release flag";

    private static final Map<String, String> CONTEXT_SELECTORS = new HashMap<>();
    public static final List<String> FLAGS = List.of("!silent", "!load", "!release", "!retain");

    private OMTLoadableMetaType() {
        super("OMT Loadable");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final HashMap<Integer, String> parameterNames = new HashMap<>();
    private static final HashMap<Integer, Set<OntResource>> parameterTypes = new HashMap<>();
    private static final Set<String> required = Set.of("path", "schema");

    public static Map<String, String> getContextSelectors() {
        return CONTEXT_SELECTORS;
    }

    static {
        features.put("id", YamlStringType::getInstance);
        features.put("path", OMTLoadablePathMetaType::getInstance);
        features.put("schema", OMTLoadableSchemaMetaType::getInstance);

        parameterNames.put(0, "$context");

        parameterTypes.put(0, Collections.singleton(OntologyModelConstants.getXsdStringInstance()));

        CONTEXT_SELECTORS.put("local", "returns the calling context");
        CONTEXT_SELECTORS.put("parent", "returns the parent of the calling context");
        CONTEXT_SELECTORS.put("omt", "returns the outermost context of the calling OMT context (Activity, Procedure, etc.)");
        CONTEXT_SELECTORS.put("session", "returns the context of the session that the calling context is in");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return required;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping, Context context) {
        return Set.of(OntologyModelConstants.getJsonObject());
    }

    @Override
    public boolean isVoid(YAMLMapping mapping) {
        return false;
    }

    @Override
    public boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources) {
        return false;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        return Collections.emptySet();
    }

    @Override
    public CallableType getType() {
        return CallableType.LOADABLE;
    }

    @Override
    public String getDocumentationClass() {
        return "Loadable";
    }

    @Override
    public List<String> getFlags() {
        return FLAGS;
    }

    @Override
    public Set<String> getAcceptableValues(int index) {
        if (index == 0) {
            return CONTEXT_SELECTORS.keySet();
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public int minNumberOfArguments(YAMLMapping mapping) {
        return 0;
    }

    @Override
    public int maxNumberOfArguments(YAMLMapping mapping) {
        return 1;
    }

    @Override
    public Map<Integer, String> getParameterNames(YAMLMapping mapping) {
        return parameterNames;
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes(YAMLMapping mapping) {
        return parameterTypes;
    }

    @Override
    public void validate(YAMLMapping mapping, PsiCall call, ProblemsHolder holder) {
        String flag = call.getFlag();
        if (flag == null) {
            return;
        }
        boolean retainFlag = flag.contains("!retain");
        boolean releaseFlag = flag.contains("!release");
        boolean silentFlag = flag.contains("!silent");
        boolean loadFlag = flag.contains("!load");
        int numberOfArguments = call.getNumberOfArguments();
        if (numberOfArguments > 0 && !(retainFlag || releaseFlag)) {
            holder.registerProblem(call, CALLING_WITH_RETAIN_OR_RELEASE_FLAG, ProblemHighlightType.WARNING);
        }
        if (releaseFlag && (retainFlag || silentFlag || loadFlag)) {
            holder.registerProblem(call, RELEASE_FLAG_SHOULD_NOT_BE_COMBINED_WITH_OTHER_FLAGS, ProblemHighlightType.WARNING);
        }
    }

    @Override
    public void validateValue(PsiCall call, ProblemsHolder holder, int i) {
        if (i != 0) {
            return;
        }
        Set<String> paramValues = CONTEXT_SELECTORS.keySet();
        if (paramValues.isEmpty() || !call.isPrimitiveArgument(i)) {
            // when not a primitive argument, we cannot compare it to the set with fixed values
            return;
        }

        String argumentValue = call.getSignatureValue(i);

        // strip the quotes before comparing
        if (argumentValue != null) {
            argumentValue = argumentValue.replaceAll("^['\"](.*)['\"]$", "$1");
            PsiElement signatureArgumentElement = call.getCallSignatureArgumentElement(i);
            OntologyValidationUtil.getInstance(holder.getProject()).validateValues(paramValues, argumentValue, holder, signatureArgumentElement);
        }

    }
}
