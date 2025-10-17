using Hypnode.Core;

namespace Hypnode.System.IO
{
    public class Assert : INode
    {
        private IConnection<bool>? inputPort = null;

        public void SetInput(string portName, IConnection<bool> connection)
        {
            if (portName == "IN") inputPort = connection;
        }

        public async Task ExecuteAsync()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            var packet = inputPort.Receive();

            if (!packet)
                throw new InvalidOperationException("Assertion failed");
        }
    }
}
