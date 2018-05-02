package it.unibz.inf.ontop.model.atom.impl;

import com.google.common.collect.ImmutableList;
import it.unibz.inf.ontop.model.atom.Context;
import it.unibz.inf.ontop.model.atom.TriplePredicate;
import it.unibz.inf.ontop.model.term.ImmutableTerm;
import it.unibz.inf.ontop.model.term.Variable;
import it.unibz.inf.ontop.model.type.TermType;

import java.util.Optional;

public class TriplePredicateImpl extends RDFAtomPredicateImpl implements TriplePredicate {

    protected TriplePredicateImpl(ImmutableList<TermType> expectedBaseTypes) {
        super("triple", 3, expectedBaseTypes, 0, 1, 2);
    }

    @Override
    public Optional<Context> getContext(ImmutableList<? extends ImmutableTerm> atomArguments) {
        return Optional.empty();
    }

    @Override
    public <T extends ImmutableTerm> ImmutableList<T>  updateSPO(ImmutableList<T> originalArguments, T newSubject,
                                                                 T newProperty, T newObject) {
        return ImmutableList.of(newSubject, newProperty, newObject);
    }
}
