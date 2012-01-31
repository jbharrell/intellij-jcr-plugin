package velir.intellij.cq5.actions.content;

import velir.intellij.cq5.jcr.model.VNode;
import velir.intellij.cq5.jcr.model.VNodeDefinition;

public class NewTemplate extends ANewNode {
	@Override
	public VNode getNode() {
		return new VNode("newTemplate", VNodeDefinition.CQ_TEMPLATE);
	}
}
