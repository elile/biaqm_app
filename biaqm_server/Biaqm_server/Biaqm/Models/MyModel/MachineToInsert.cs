using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class MachineToInsert
    {
        public long id { get; set; }
        public String totalTime { get; set; }
        public MachineAdvancedResult machineAdvanceResult { get; set; }
    }
}