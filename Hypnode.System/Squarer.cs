using Hypnode.Core;

namespace Hypnode.System
{
    public class Squarer : INode
    {
        private IConnection<int> inputPort;
        private IConnection<int> outputPort;

        public void SetInput(string portName, IConnection<int> connection)
        {
            if (portName == "IN") inputPort = connection;
        }

        public void SetOutput(string portName, IConnection<int> connection)
        {
            if (portName == "OUT") outputPort = connection;
        }

        public void Execute()
        {
            Console.WriteLine("Squarer: Ready to process data...");
            try
            {
                while (true)
                {
                    var packet = inputPort.Buffer.Take();
                    var result = packet * packet;
                    outputPort?.Send(result);
                }
            }
            catch (InvalidOperationException)
            {
                Console.WriteLine("Squarer: Input stream closed. Finished processing.");
            }
            finally
            {
                outputPort?.Close();
            }
        }
    }
}
