using Hypnode.Core;

namespace Hypnode.System.Math
{
    public class Squarer : INode
    {
        private Connection<int>? inputPort = null;
        private Connection<int>? outputPort = null;

        public Squarer SetInput(string portName, Connection<int> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public Squarer SetOutput(string portName, Connection<int> connection)
        {
            if (portName == "OUT") outputPort = connection;
            return this;
        }

        public async Task ExecuteAsync()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            while (true)
            {
                var packet = inputPort.Receive();
                var result = packet * packet;
                outputPort?.Send(result);
            }
        }
    }
}
