using Hypnode.Core;

namespace Hypnode.Logic
{
    public class NotGate : INode
    {
        private IConnection<LogicValue>? inputPort = null;
        private IConnection<LogicValue>? outputPort = null;

        public NotGate SetInput(string portName, IConnection<LogicValue> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public NotGate SetOutput(string portName, IConnection<LogicValue> connection)
        {
            if (portName == "OUT") outputPort = connection;
            return this;
        }

        public void Execute()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port A is not set");

            while (true)
            {
                var packet = inputPort.Receive();

                if (packet == LogicValue.True)
                    outputPort?.Send(LogicValue.False);
                else
                    outputPort?.Send(LogicValue.True);
            }
        }
    }
}
