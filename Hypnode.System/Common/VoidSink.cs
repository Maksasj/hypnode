using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class VoidSink<T> : INode
    {
        private List<Connection<T>> inputPorts;

        public VoidSink()
        {
            inputPorts = [];
        }

        public INode SetPort(string portName, IConnection connection)
        {
            if (connection is Connection<T> conn) inputPorts.Add(conn);

            return this;
        }

        public async Task ExecuteAsync()
        {
            if (inputPorts.Count == 0)
                return;

            var consumptionTasks = new List<Task>();

            foreach (var connection in inputPorts)
                consumptionTasks.Add(Task.Run(() => VoidSink<T>.ConsumeConnection(connection)));

            await Task.WhenAll(consumptionTasks);
        }

        private static void ConsumeConnection(Connection<T> connection)
        {
            while (connection.TryReceive(out _))
            {

            }
        }
    }
}
