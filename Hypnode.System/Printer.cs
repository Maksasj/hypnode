using Hypnode.Core;

namespace Hypnode.System
{
    public class Printer<T> : INode
    {
        private IConnection<T> inputPort;

        public void SetInput(string portName, IConnection<T> connection)
        {
            if (portName == "IN") inputPort = connection;
        }

        public void Execute()
        {
            while (true)
            {
                var packet = inputPort.Buffer.Take();
                Console.WriteLine($"{packet}");
            }
        }
    }
}
