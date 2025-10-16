using Hypnode.Core;

namespace Hypnode.Logic
{
    public class And : INode
    {
        private IConnection<LogicValue>? inputPortA = null;
        private IConnection<LogicValue>? inputPortB = null;
        private IConnection<LogicValue>? outputPort = null;

        public And SetInput(string portName, IConnection<LogicValue> connection)
        {
            if (portName == "INA") inputPortA = connection;
            if (portName == "INB") inputPortB = connection;
            return this;
        }

        public And SetOutput(string portName, IConnection<LogicValue> connection)
        {
            if (portName == "OUT") outputPort = connection;
            return this;
        }

        public void Execute()
        {
            if (inputPortA is null)
                throw new InvalidOperationException("Input port A is not set");

            if (inputPortB is null)
                throw new InvalidOperationException("Input port B is not set");

            while (true)
            {
                var packetA = inputPortA.Receive();
                var packetB = inputPortB.Receive();

                if (packetA == LogicValue.True && packetB == LogicValue.True)
                    outputPort?.Send(LogicValue.True);
                else
                    outputPort?.Send(LogicValue.False);
            }
        }
    }
}
