using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class Splitter<T> : INode
    {
        private Connection<T>? inputPort = null;
        private readonly List<Connection<T>> outputPorts;

        public Splitter()
        {
            outputPorts = [];
        }

        public Splitter<T> SetInput(string portName, Connection<T> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public Splitter<T> AddOutput(Connection<T> connection)
        {
            outputPorts.Add(connection);
            return this;
        }

        public async Task ExecuteAsync()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            while (inputPort.TryReceive(out var packet))
            {
                foreach (var connection in outputPorts)
                    connection?.Send(packet);
            }

            foreach (var connection in outputPorts)
                connection?.Close();
        }
    }
}
