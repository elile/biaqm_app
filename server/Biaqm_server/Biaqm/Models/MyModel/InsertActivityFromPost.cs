using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Biaqm.Models.MyModel
{
    public class InsertActivityFromPost
    {

        public String ActivityType_id { get; set; }
        public String CompanyID { get; set; }
        public String farm_id { get; set; }
        public String Remarks { get; set; }
        public String BlockID { get; set; }
        public String PlotID { get; set; }
        public String CropID { get; set; }
        public String VarietyID { get; set; }
        public String date { get; set; }

        public WorkerToInsert[] workerToInsert { get; set; }
        public MachineToInsert[] machineToInsert { get; set; }


    }
}