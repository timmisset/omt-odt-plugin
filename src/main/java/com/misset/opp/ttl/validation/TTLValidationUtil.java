package com.misset.opp.ttl.validation;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.ResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;

import java.util.Set;

public class TTLValidationUtil {

    public static boolean validateCompatibleTypes(Set<OntResource> resourcesA,
                                                  Set<OntResource> resourcesB,
                                                  ProblemsHolder holder,
                                                  PsiElement element) {
        if (!OppModel.INSTANCE.areCompatible(resourcesA, resourcesB)) {
            holder.registerProblem(element,
                    createWarning(resourcesA, resourcesB),
                    ProblemHighlightType.WARNING);
            return false;
        }
        return true;
    }

    public static boolean validateModularityMultiple(Set<OntResource> subject,
                                                     Property predicate,
                                                     ProblemsHolder holder,
                                                     PsiElement element) {
        if (OppModel.INSTANCE.isSingleton(subject, predicate)) {
            // assigning a collection or creating a collection where a singleton is expected
            holder.registerProblem(element,
                    "Suspicious assignment: " + predicate.getLocalName() + " maxCount == 1",
                    ProblemHighlightType.WARNING);
            return false;
        }
        return true;
    }

    private static String createWarning(Set<OntResource> resourcesA,
                                        Set<OntResource> resourcesB) {
        return "Incompatible types:" + "\n" +
                "cannot assign " + "\n" +
                ResourceUtil.describeUrisJoined(resourcesB) + "\n" +
                "to" + "\n" +
                ResourceUtil.describeUrisJoined(resourcesA);
    }

}
