using Hypnode.Core;

namespace Hypnode.Logic.Gates
{
    public class OrGate : INode
    {
        private Connection<LogicValue>? inputPortA = null;
        private Connection<LogicValue>? inputPortB = null;
        private Connection<LogicValue>? outputPort = null;

        public OrGate SetInput(string portName, Connection<LogicValue> connection)
        {
            if (portName == "INA") inputPortA = connection;
            if (portName == "INB") inputPortB = connection;
            return this;
        }

        public OrGate SetOutput(string portName, Connection<LogicValue> connection)
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
                var result = (packetA == LogicValue.True || packetB == LogicValue.True) ? LogicValue.True : LogicValue.False;
                outputPort?.Send(result);
            }

            outputPort?.Close();
        }
    }
}