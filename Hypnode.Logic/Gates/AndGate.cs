using Hypnode.Core;

namespace Hypnode.Logic.Gates
{
    public class AndGate : INode
    {
        private IConnection<LogicValue>? inputPortA = null;
        private IConnection<LogicValue>? inputPortB = null;
        private IConnection<LogicValue>? outputPort = null;

        public AndGate SetInput(string portName, IConnection<LogicValue> connection)
        {
            if (portName == "INA") inputPortA = connection;
            if (portName == "INB") inputPortB = connection;
            return this;
        }

        public AndGate SetOutput(string portName, IConnection<LogicValue> connection)
        {
            if (portName == "OUT") outputPort = connection;
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
                var result = (packetA == LogicValue.True && packetB == LogicValue.True) ? LogicValue.True : LogicValue.False;
                outputPort?.Send(result);
            }

            outputPort?.Close();
        }
    }
}
