using Hypnode.Core;

namespace Hypnode.System.Math
{
    public class Squarer : INode
    {
        private Connection<int>? inputPort = null;
        private Connection<int>? outputPort = null;

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "INA" && connection is Connection<int> con0) inputPort = con0;
            if (portName == "OUT" && connection is Connection<int> con1) outputPort = con1;

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
