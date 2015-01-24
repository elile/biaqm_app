using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class WorkerAdvancedResult
    {
        public Worker w { get; set; }
        public int workerQuantity { get; set; }
        public double totalTime { get; set; }// need to split to extra hour...
        public Yield_Descriptions yd { get; set; }
        public int numOfYd { get; set; }
        public double costOfYd { get; set; }
        public String remarks { get; set; }

        public String StartWorkingHour { get; set; }
        public String EndWorkingHour { get; set; }
    }
}