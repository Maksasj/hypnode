using Hypnode.Core;

namespace Hypnode.Async
{
    public class AsyncNodeGraph : INodeGraph
    {
        public List<INode> Nodes { get; set; }

        public AsyncNodeGraph()
        {
            Nodes = [];
        }

        public IConnection<T> CreateConnection<T>() => new AsyncConnection<T>();

        public T AddNode<T>(T node) where T : INode
        {
            Nodes.Add(node);
            return node;
        }

        public void Evaluate()
        {
            var tasks = new List<Task>();

            foreach(var node in Nodes) 
                tasks.Add(Task.Run(() => node.Execute()));

            Task.WaitAll([.. tasks]);
        }
    }
}
