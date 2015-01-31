using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class WorkerToInsert
    {
        public long id { get; set; }
        public String totalTime { get; set; }
        public WorkerAdvancedResult workerAdvancedResult { get; set; }
    }
}