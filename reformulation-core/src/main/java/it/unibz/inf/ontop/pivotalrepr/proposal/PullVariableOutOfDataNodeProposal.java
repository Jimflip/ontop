package it.unibz.inf.ontop.pivotalrepr.proposal;

import com.google.common.collect.ImmutableList;
import it.unibz.inf.ontop.pivotalrepr.DataNode;

/**
 * TODO: explain
 *
 * TODO: make explicit the treatment that is expected to be done
 *
 */
public interface PullVariableOutOfDataNodeProposal extends SimpleNodeCentricOptimizationProposal<DataNode> {

    /**
     * Indexes of the variables to renamed.
     *
     * Indexes inside the focus node atom.
     */
    ImmutableList<Integer> getIndexes();

}
