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

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "IN" && connection is Connection<T> con0) inputPort = con0;
            if (portName == "OUT" && connection is Connection<T> con1) outputPorts.Add(con1);

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
