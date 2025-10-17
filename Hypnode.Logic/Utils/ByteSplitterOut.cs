using Hypnode.Core;

namespace Hypnode.Logic.Utils
{
    public class ByteSplitterOut : INode
    {
        private readonly Connection<LogicValue>[] inputPorts;
        private Connection<byte>? outputPort = null;

        public ByteSplitterOut()
        {
            inputPorts = new Connection<LogicValue>[8];
        }

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "0" && connection is Connection<LogicValue> conn0) inputPorts[0] = conn0;
            if (portName == "1" && connection is Connection<LogicValue> conn1) inputPorts[1] = conn1;
            if (portName == "2" && connection is Connection<LogicValue> conn2) inputPorts[2] = conn2;
            if (portName == "3" && connection is Connection<LogicValue> conn3) inputPorts[3] = conn3;
            if (portName == "4" && connection is Connection<LogicValue> conn4) inputPorts[4] = conn4;
            if (portName == "5" && connection is Connection<LogicValue> conn5) inputPorts[5] = conn5;
            if (portName == "6" && connection is Connection<LogicValue> conn6) inputPorts[6] = conn6;
            if (portName == "7" && connection is Connection<LogicValue> conn7) inputPorts[7] = conn7;
            if (portName == "OUT" && connection is Connection<byte> conn8) outputPort = conn8;

            return this;
        }

        public async Task ExecuteAsync()
        {
            bool allReceived;
            LogicValue[] receivedValues = new LogicValue[8];

            do
            {
                allReceived = true;

                for (int i = 0; i < 8; i++)
                {
                    if (inputPorts[i] is null)
                    {
                        throw new InvalidOperationException($"Input port {i} is not set");
                    }

                    if (inputPorts[i]!.TryReceive(out var value))
                    {
                        receivedValues[i] = value;
                    }
                    else
                    {
                        allReceived = false;
                        break;
                    }
                }

                if (allReceived)
                {
                    byte outputByte = (byte)Enumerable.Range(0, 8)
                        .Select(i => (receivedValues[i] == LogicValue.True) ? (1 << i) : 0)
                        .Aggregate(0, (current, bitValue) => current | bitValue);

                    outputPort?.Send(outputByte);
                }
            } while (allReceived);

            outputPort?.Close();
        }
    }
}
