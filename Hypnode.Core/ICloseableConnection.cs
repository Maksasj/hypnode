using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Hypnode.Core
{
    public interface ICloseableConnection
    {
        public void Close();
    }
}
