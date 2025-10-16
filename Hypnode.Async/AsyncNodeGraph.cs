using Hypnode.Core;

namespace Hypnode.Async
{
    public class AsyncNodeGraph : INodeGraph
    {
        public List<INode> Nodes { get; set; }
        public List<ICloseableConnection> Connections { get; set; }

        public AsyncNodeGraph()
        {
            Nodes = [];
            Connections = [];
        }

        public IConnection<T> CreateConnection<T>()
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
            var tasks = new List<Task>();

            foreach (var node in Nodes)
                tasks.Add(Task.Run(() => node.Execute()));

            Task.WaitAll([.. tasks]);
        }

        public async Task EvaluateAsync(TimeSpan timeout)
        {
            var nodeExecutionTasks = Nodes.Select(node => Task.Run(() => node.Execute())).ToList();
            await Task.WhenAll(nodeExecutionTasks);
        }
    }
}
