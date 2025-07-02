import React from 'react';

function StatsTable({ totalNonWorking, totalOffice, totalWFH, inOfficeDays, actualConnectionPercent, connectionColor, totalWorkingDays }) {
  return (
    <table className="stats-table">
      <tbody>
        <tr>
          <td>Actual Connection %</td>
          <td className={connectionColor}>{actualConnectionPercent}%</td>
        </tr>
        <tr>
          <td>Target Connection %</td>
          <td>50%</td>
        </tr>
        <tr>
          <td>Required Office Days</td>
          <td>{inOfficeDays}</td>
        </tr>
        <tr>
          <td>Total WFH Days</td>
          <td>{totalWFH}</td>
        </tr>
        <tr>
          <td>Total Office Days</td>
          <td>{totalOffice}</td>
        </tr>
        <tr>
          <td>Total Non-Working Days</td>
          <td>{totalNonWorking}</td>
        </tr>
        <tr>
          <td>Total Working Days</td>
          <td>{totalWorkingDays}</td>
        </tr>
      </tbody>
    </table>
  );
}

export default StatsTable;