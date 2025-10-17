using Hypnode.Core;

namespace Hypnode.Async
{
    public class AsyncNodeGraph : INodeGraph
    {
        public List<INode> Nodes { get; set; }
        public List<IConnection> Connections { get; set; }

        public AsyncNodeGraph()
        {
            Nodes = [];
            Connections = [];
        }

        public Connection<T> CreateConnection<T>()
        {
            var connection = new AsyncConnection<T>();
            Connections.Add(connection);
            return connection;
        }

        public T AddNode<T>(T node) where T : INode
        {
            Nodes.Add(node);
            return node;
        }

        public async Task EvaluateAsync()
        {
            var executionTasks = Nodes.Select(node => Task.Run(async () => await node.ExecuteAsync())).ToList();
            await Task.WhenAll(executionTasks);
        }
    }
}
