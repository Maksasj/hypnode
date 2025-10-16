namespace Hypnode.Core
{
    public interface INodeGraph
    {
        public IConnection<T> CreateConnection<T>();
    }
}
