using Hypnode.Async;
using Hypnode.System;

namespace Hypnode.Example
{
    class Program
    {
        public static void Main()
        {
            var graph = new AsyncNodeGraph();
            var conn1 = graph.CreateConnection<int>();
            var conn2 = graph.CreateConnection<int>();

            graph.AddNode(new Generator())
                .SetOutput("OUT", conn1);
            
            graph.AddNode(new Squarer())
                .SetInput("IN", conn1)
                .SetOutput("OUT", conn2);

            graph.AddNode(new Printer<int>())
                .SetInput("IN", conn2);

            graph.Evaluate();
        }
    }
}