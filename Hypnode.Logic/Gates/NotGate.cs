using Hypnode.Core;

namespace Hypnode.Logic.Gates
{
    public class NotGate : INode
    {
        private Connection<LogicValue>? inputPort = null;
        private Connection<LogicValue>? outputPort = null;

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "IN" && connection is Connection<LogicValue> con0) inputPort = con0;
            if (portName == "OUT" && connection is Connection<LogicValue> con1) outputPort = con1;

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
