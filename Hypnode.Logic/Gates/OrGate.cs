using Hypnode.Core;

namespace Hypnode.Logic.Gates
{
    public class OrGate : INode
    {
        private Connection<LogicValue>? inputPortA = null;
        private Connection<LogicValue>? inputPortB = null;
        private Connection<LogicValue>? outputPort = null;

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "INA" && connection is Connection<LogicValue> con0) inputPortA = con0;
            if (portName == "INB" && connection is Connection<LogicValue> con1) inputPortB = con1;
            if (portName == "OUT" && connection is Connection<LogicValue> con2) outputPort = con2;

            return this;
        }

        public async Task ExecuteAsync()
        {
            if (inputPortA is null)
                throw new InvalidOperationException("Input port A is not set");

            if (inputPortB is null)
                throw new InvalidOperationException("Input port B is not set");

            while (inputPortA.TryReceive(out var packetA) && inputPortB.TryReceive(out var packetB))
            {
                var result = (packetA == LogicValue.True || packetB == LogicValue.True) ? LogicValue.True : LogicValue.False;
                outputPort?.Send(result);
            }

            outputPort?.Close();
        }
    }
}