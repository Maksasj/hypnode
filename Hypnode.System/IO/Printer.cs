using Hypnode.Core;

namespace Hypnode.System.IO
{
    public class Printer<T> : INode
    {
        private IConnection<T>? inputPort = null;

        public void SetInput(string portName, IConnection<T> connection)
        {
            if (portName == "IN") inputPort = connection;
        }

        public void Execute()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            while (true)
            {
                var packet = inputPort.Receive();
                Console.WriteLine($"{packet}");
            }
        }
    }
}
