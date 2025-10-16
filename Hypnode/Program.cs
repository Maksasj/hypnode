using Hypnode.Core;
using Hypnode.System;

namespace Hypnode.Example
{
    class Program
    {
        public static void Main()
        {
            var generator = new Generator();
            var squarer = new Squarer();
            var printer = new Printer<int>();

            var conn1 = new IConnection<int>();
            var conn2 = new IConnection<int>();

            generator.SetOutput("OUT", conn1);
            squarer.SetInput("IN", conn1);

            squarer.SetOutput("OUT", conn2);
            printer.SetInput("IN", conn2);

            Console.WriteLine("--- Starting Flow-Based Network ---");

            var task1 = Task.Run(() => generator.Execute());
            var task2 = Task.Run(() => squarer.Execute());
            var task3 = Task.Run(() => printer.Execute());

            Task.WaitAll(task1, task2, task3);

            Console.WriteLine("--- Flow-Based Network Finished ---");
        }
    }
}