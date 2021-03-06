package MakePlus;

import OpenRate.adapter.jdbc.JDBCBatchOutputAdapter;
import OpenRate.adapter.jdbc.JDBCOutputAdapter;
import OpenRate.record.DBRecord;
import OpenRate.record.IRecord;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author IanAdmin
 */
public class RejectOutput extends JDBCBatchOutputAdapter
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: RejectOutput.java,v $, $Revision: 1.9 $, $Date: 2011/08/11 08:56:44 $";

  /** Creates a new instance of DBOutput */
  public RejectOutput()
  {
    super();
  }

  /**
   * We transform the records here so that they are ready to output making any
   * specific changes to the record that are necessary to make it ready for
   * output.
   *
   * As we are using the JDBCOutput adapter, we should transform the records
   * into DBRecords, storing the data to be written using the SetData() method.
   * This means that we do not have to know about the internal workings of the
   * output adapter.
   *
   * Note that this is just undoing the transformation that we did in the input
   * adapter.
   */
    @Override
    public Collection<IRecord> procValidRecord(IRecord r)
    {
      return null;
    }

  /**
   * Handle any error records here so that they are ready to output making any
   * specific changes to the record that are necessary to make it ready for
   * output.
   */
    @Override
    public Collection<IRecord> procErrorRecord(IRecord r)
    {
      DBRecord tmpDataRecord;
      CDRRecord tmpInRecord;
      Long      currentTime = System.currentTimeMillis()/1000;

      Collection<IRecord> Outbatch;
      Outbatch = new ArrayList<IRecord>();
      tmpInRecord = (CDRRecord)r;
      tmpDataRecord = new DBRecord();
      tmpDataRecord.setOutputColumnCount(25);
      
      tmpDataRecord.setOutputColumnString(0,tmpInRecord.EntryDate);
      tmpDataRecord.setOutputColumnString(1,tmpInRecord.callReference);
      tmpDataRecord.setOutputColumnString(2,tmpInRecord.Network);
      tmpDataRecord.setOutputColumnString(3,tmpInRecord.CallType);
      tmpDataRecord.setOutputColumnString(4,tmpInRecord.RemoteNetwork);
      tmpDataRecord.setOutputColumnString(5,tmpInRecord.RemoteSwitch);
      tmpDataRecord.setOutputColumnString(6,tmpInRecord.Direction);
      tmpDataRecord.setOutputColumnString(7,tmpInRecord.PortingPrefix);
      tmpDataRecord.setOutputColumnString(8,tmpInRecord.CLI);
      tmpDataRecord.setOutputColumnString(9,tmpInRecord.Destination);
      tmpDataRecord.setOutputColumnString(10,tmpInRecord.UsageType);
      tmpDataRecord.setOutputColumnString(11,tmpInRecord.NumberType);
      tmpDataRecord.setOutputColumnString(12,tmpInRecord.StartDate);
      tmpDataRecord.setOutputColumnDouble(13,tmpInRecord.Duration);
      tmpDataRecord.setOutputColumnString(14,tmpInRecord.GuidingKey);
      tmpDataRecord.setOutputColumnString(15,tmpInRecord.DisplayNumber);
      tmpDataRecord.setOutputColumnString(16,tmpInRecord.Platform);
      tmpDataRecord.setOutputColumnString(17,tmpInRecord.Machine);
      tmpDataRecord.setOutputColumnString(18,tmpInRecord.LinkNumber);
      tmpDataRecord.setOutputColumnString(19,tmpInRecord.OriginalData);
      tmpDataRecord.setOutputColumnString(20,tmpInRecord.getErrors().get(0).getMessage());
      tmpDataRecord.setOutputColumnString(21,tmpInRecord.SuspenseCategory);
      tmpDataRecord.setOutputColumnLong  (22,currentTime);
      tmpDataRecord.setOutputColumnString(23,tmpInRecord.getErrors().get(0).getErrorDescription());
      tmpDataRecord.setOutputColumnString(24,tmpInRecord.UID);

      Outbatch.add((IRecord)tmpDataRecord);

      return Outbatch;
    }
}
