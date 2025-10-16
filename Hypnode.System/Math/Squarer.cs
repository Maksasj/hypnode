using Hypnode.Core;

namespace Hypnode.System.Math
{
    public class Squarer : INode
    {
        private IConnection<int>? inputPort = null;
        private IConnection<int>? outputPort = null;

        public Squarer SetInput(string portName, IConnection<int> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public Squarer SetOutput(string portName, IConnection<int> connection)
        {
            if (portName == "OUT") outputPort = connection;
            return this;
        }

        public void Execute()
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
