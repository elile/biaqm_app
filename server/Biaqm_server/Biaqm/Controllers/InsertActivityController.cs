using Biaqm.Dal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Biaqm.Controllers
{
    public class InsertActivityController : ApiController /*TODO check this*/
    {
        [HttpGet]
        public Int64 InsertActivityPlan(
        String FromDate,
        String ToDate,
        String ActivityType_id,
        String CompanyID,
        String farm_id,
        String Remarks,
        String BlockID,
        String PlotID,
        String CropID,
        String VarietyID,
        String WorkerID,
        double totalTime,
        String MachineryID,
        String MachineryHours,
        String MotorizedMachineryID,
        String MotorizedMachineryHours
        )
        {
            return InsertActivityDal.InsertActivityPlan(
                FromDate,
            ToDate,
            ActivityType_id,
            CompanyID,
            farm_id,
            Remarks == null ? "" : Remarks,
            BlockID,
            PlotID,
            CropID,
            VarietyID,
            WorkerID == null ? "-1" : WorkerID,
            totalTime == null ? 0 : totalTime,
            MachineryID == null ? "-1" : MachineryID,
            MachineryHours == null ? "0" : MachineryHours,
            MotorizedMachineryID == null ? "-1" : MotorizedMachineryID,
            MotorizedMachineryHours == null ? "0" : MotorizedMachineryHours
            );
        }

        [HttpGet]
        public Int64 InsertActivity(
        String FromDate,
        String ToDate,
        String ActivityType_id,
        String CompanyID,
        String farm_id,
        String Remarks,
        String BlockID,
        String PlotID,
        String CropID,
        String VarietyID,
        String WorkerID,
        double totalTime,
        String MachineryID,
        String MachineryHours,
        String MotorizedMachineryID,
        String MotorizedMachineryHours
        )
        {
            return InsertActivityDal.InsertActivity(
            FromDate,
            ToDate,
            ActivityType_id,
            CompanyID,
            farm_id,
            Remarks == null ? "" : Remarks,
            BlockID,
            PlotID,
            CropID,
            VarietyID,
            WorkerID == null ? "-1" : WorkerID,
            totalTime == null ? 0 : totalTime,
            MachineryID == null ? "-1" : MachineryID,
            MachineryHours == null ? "0" : MachineryHours,
            MotorizedMachineryID == null ? "-1" : MotorizedMachineryID,
            MotorizedMachineryHours == null ? "0" : MotorizedMachineryHours
            );
        }
    }
}
