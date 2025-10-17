using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class Demux<T> : INode
    {
        private IConnection<T>? inputPort = null;
        private readonly List<IConnection<T>> outputPorts;

        public Demux()
        {
            outputPorts = [];
        }

        public Demux<T> SetInput(string portName, IConnection<T> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public Demux<T> AddOutput(IConnection<T> connection)
        {
            outputPorts.Add(connection);
            return this;
        }

        public async Task ExecuteAsync()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            // Demux buggy: should send each packet to all output ports
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
