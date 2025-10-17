using Hypnode.Core;

namespace Hypnode.Logic.Gates
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

        public async Task ExecuteAsync()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port A is not set");

            while (inputPort.TryReceive(out var packet))
            {
                var result = (packet == LogicValue.True) ? LogicValue.False : LogicValue.True;
                outputPort?.Send(result);
            }

            outputPort?.Close();
        }
    }
}
