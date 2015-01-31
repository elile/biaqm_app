using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Biaqm.Models.MyModel
{
    public class MachineAdvancedResult
    {
        public motoring_machinery m { get; set; }
        public double totalTime { get; set; }
        public Yield_Descriptions yd { get; set; }
        public int numOfYd { get; set; }
        public double costOfYd { get; set; }
        public String remarks { get; set; }
	
    }
}
