namespace Hypnode.Core
{
    public abstract class ICompoundNode : INode
    {
        public INodeGraph NodeGraph { init; protected get; }

        public ICompoundNode(INodeGraph nodeGraph)
        {
            NodeGraph = nodeGraph;
        }

        public abstract Task ExecuteAsync();
    }
}
