namespace Hypnode.Core
{
    public interface INode
    {
        public INode SetPort(string portName, IConnection connection);

        Task ExecuteAsync();
    }
}
