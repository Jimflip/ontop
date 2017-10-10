package it.unibz.inf.ontop.model.type.impl;

import it.unibz.inf.ontop.model.type.*;
import org.eclipse.rdf4j.model.IRI;


public class ConcreteNumericRDFDatatypeImpl extends SimpleRDFDatatype implements ConcreteNumericRDFDatatype {

    private final TypePropagationSubstitutionHierarchy promotedHierarchy;

    private ConcreteNumericRDFDatatypeImpl(IRI datatypeIRI, TermTypeAncestry parentAncestry,
                                             TypePropagationSubstitutionHierarchy promotedParentHierarchy,
                                             COL_TYPE colType, boolean appendToPromotedHierarchy) {
        super(colType, parentAncestry, false, datatypeIRI);
        promotedHierarchy = appendToPromotedHierarchy ?
                promotedParentHierarchy.newHierarchy(this)
                : promotedParentHierarchy;
    }

    private ConcreteNumericRDFDatatypeImpl(IRI datatypeIRI, TermTypeAncestry parentAncestry,
                                           TypePropagationSubstitutionHierarchy promotedParentHierarchy,
                                           boolean appendToPromotedHierarchy) {
        super(datatypeIRI, parentAncestry, false);
        promotedHierarchy = appendToPromotedHierarchy ?
                promotedParentHierarchy.newHierarchy(this)
                : promotedParentHierarchy;
    }

    private ConcreteNumericRDFDatatypeImpl(IRI datatypeIRI, TermTypeAncestry parentAncestry, COL_TYPE colType) {
        super(colType, parentAncestry, false, datatypeIRI);
        promotedHierarchy = new TypePropagationSubstitutionHierarchyImpl(this);
    }

    @Override
    public TypePropagationSubstitutionHierarchy getPromotionSubstitutionHierarchy() {
        return promotedHierarchy;
    }

    static ConcreteNumericRDFDatatype createTopConcreteNumericTermType(IRI datatypeIRI,
                                                                       NumericRDFDatatype abstractParentDatatype,
                                                                       COL_TYPE colType) {
        if (!abstractParentDatatype.isAbstract())
            throw new IllegalArgumentException("The parent datatype must be abstract");

        return new ConcreteNumericRDFDatatypeImpl(datatypeIRI, abstractParentDatatype.getAncestry(), colType);
    }

    static ConcreteNumericRDFDatatype createConcreteNumericTermType(IRI datatypeIRI,
                                                                    ConcreteNumericRDFDatatype parentDatatype,
                                                                    COL_TYPE colType,
                                                                    boolean appendToPromotedHierarchy) {
        return createConcreteNumericTermType(datatypeIRI, parentDatatype.getAncestry(),
                parentDatatype.getPromotionSubstitutionHierarchy(), colType, appendToPromotedHierarchy);
    }

    static ConcreteNumericRDFDatatype createConcreteNumericTermType(IRI datatypeIRI, ConcreteNumericRDFDatatype parentDatatype,
                                                                    boolean appendToPromotedHierarchy) {
        return new ConcreteNumericRDFDatatypeImpl(datatypeIRI, parentDatatype.getAncestry(),
                parentDatatype.getPromotionSubstitutionHierarchy(), appendToPromotedHierarchy);
    }

    static ConcreteNumericRDFDatatype createConcreteNumericTermType(IRI datatypeIRI, TermTypeAncestry parentAncestry,
                                                                    TypePropagationSubstitutionHierarchy promotedParentHierarchy,
                                                                    boolean appendToPromotedHierarchy) {

        return new ConcreteNumericRDFDatatypeImpl(datatypeIRI, parentAncestry, promotedParentHierarchy,
                appendToPromotedHierarchy);
    }

    static ConcreteNumericRDFDatatype createConcreteNumericTermType(IRI datatypeIRI, TermTypeAncestry parentAncestry,
                                                            TypePropagationSubstitutionHierarchy promotedParentHierarchy,
                                                            COL_TYPE colType, boolean appendToPromotedHierarchy) {

        return new ConcreteNumericRDFDatatypeImpl(datatypeIRI, parentAncestry, promotedParentHierarchy, colType,
                appendToPromotedHierarchy);
    }
}