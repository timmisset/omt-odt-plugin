package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;

class OMTStandaloneQueryMetaTypeTest extends OMTOntologyTestCase {

    @Test
    void testResolveStandaloneQuery() {
        configureByText(insideStandaloneQueryWithPrefixes("query: \n" +
                "   /ont:ClassA"));
        assertResolvedContainsResource(Collections.emptySet(),
                mock(PsiCall.class),
                OppModel.INSTANCE.getClass("http://ontology#ClassA"));
    }

    @Test
    void testResolveStandaloneQueryWithParameters() {
        configureByText(insideStandaloneQueryWithPrefixes("params:\n" +
                "   - $paramA (ont:ClassA)\n" +
                "query: $paramA"));
        assertResolvedContainsResource(Collections.emptySet(),
                mock(PsiCall.class),
                OppModel.INSTANCE.getIndividual("http://ontology#ClassA_INSTANCE"));
    }

    @Test
    void testResolveStandaloneQueryWithNonAnnotatedParameters() {
        final PsiCall call = mock(PsiCall.class);
        final Individual individual = OppModel.INSTANCE.getIndividual("http://ontology#ClassA_INSTANCE");
        final Set<OntResource> resources = Set.of(individual);
        doReturn(resources).when(call).resolveSignatureArgument(0);
        doReturn(resources).when(call).getParamType("$paramA");

        configureByText(insideStandaloneQueryWithPrefixes("params:\n" +
                "   - $paramA\n" +
                "query: $paramA"));
        assertResolvedContainsResource(Collections.emptySet(), call, individual);
        verify(call).setParamType("$paramA", resources);
    }

    private void assertResolvedContainsResource(Set<OntResource> resources,
                                                PsiCall call,
                                                OntResource resource) {
        ReadAction.run(() -> {
            final YAMLMapping mapping = PsiTreeUtil.findChildrenOfType(getFile(), YAMLKeyValue.class)
                    .stream()
                    .filter(keyValue -> keyValue.getKeyText().equals("StandaloneQuery"))
                    .map(YAMLKeyValue::getValue)
                    .map(YAMLMapping.class::cast)
                    .findFirst()
                    .orElse(null);
            doReturn(resources).when(call).resolvePreviousStep();
            Context context = Context.fromCall(call);
            final Set<OntResource> resolved = new OMTStandaloneQueryMetaType().resolve(mapping, context);
            assertContainsElements(resolved, resource);
        });
    }

}
