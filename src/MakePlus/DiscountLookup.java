/* ====================================================================
 * Limited Evaluation License:
 *
 * The exclusive owner of this work is Tiger Shore Management Ltd.
 * This work, including all associated documents and components
 * is Copyright Tiger Shore Management Ltd 2006-2010.
 *
 * The following restrictions apply unless they are expressly relaxed in a
 * contractual agreement between the license holder or one of its officially
 * assigned agents and you or your organisation:
 *
 * 1) This work may not be disclosed, either in full or in part, in any form
 *    electronic or physical, to any third party. This includes both in the
 *    form of source code and compiled modules.
 * 2) This work contains trade secrets in the form of architecture, algorithms
 *    methods and technologies. These trade secrets may not be disclosed to
 *    third parties in any form, either directly or in summary or paraphrased
 *    form, nor may these trade secrets be used to construct products of a
 *    similar or competing nature either by you or third parties.
 * 3) This work may not be included in full or in part in any application.
 * 4) You may not remove or alter any proprietary legends or notices contained
 *    in or on this work.
 * 5) This software may not be reverse-engineered or otherwise decompiled, if
 *    you received this work in a compiled form.
 * 6) This work is licensed, not sold. Possession of this software does not
 *    imply or grant any right to you.
 * 7) You agree to disclose any changes to this work to the copyright holder
 *    and that the copyright holder may include any such changes at its own
 *    discretion into the work
 * 8) You agree not to derive other works from the trade secrets in this work,
 *    and that any such derivation may make you liable to pay damages to the
 *    copyright holder
 * 9) You agree to use this software exclusively for evaluation purposes, and
 *    that you shall not use this software to derive commercial profit or
 *    support your business or personal activities.
 *
 * This software is provided "as is" and any expressed or impled warranties,
 * including, but not limited to, the impled warranties of merchantability
 * and fitness for a particular purpose are disclaimed. In no event shall
 * Tiger Shore Management or its officially assigned agents be liable to any
 * direct, indirect, incidental, special, exemplary, or consequential damages
 * (including but not limited to, procurement of substitute goods or services;
 * Loss of use, data, or profits; or any business interruption) however caused
 * and on theory of liability, whether in contract, strict liability, or tort
 * (including negligence or otherwise) arising in any way out of the use of
 * this software, even if advised of the possibility of such damage.
 * This software contains portions by The Apache Software Foundation, Robert
 * Half International.
 * ====================================================================
 */
/* ========================== VERSION HISTORY =========================
 * $Log: DiscountLookup.java,v $
 * Revision 1.1  2011/08/16 06:04:42  ian
 * Discounting initial version
 *
 * ====================================================================
 */
package MakePlus;

import OpenRate.process.AbstractRegexMatch;
import OpenRate.record.ChargePacket;
import OpenRate.record.IRecord;
import java.util.ArrayList;

/**
 * This class looks up the name of any bundles that we have to use, and
 * initialises the discount control variables for discounting.
 */
public class DiscountLookup 
  extends AbstractRegexMatch
{
  /**
   * CVS version info - Automatically captured and written to the Framework
   * Version Audit log at Framework startup. For more information
   * please <a target='new' href='http://www.open-rate.com/wiki/index.php?title=Framework_Version_Map'>click here</a> to go to wiki page.
   */
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: DiscountLookup.java,v $, $Revision: 1.1 $, $Date: 2011/08/16 06:04:42 $";

  // this is used for the lookup
  String[] tmpSearchParameters = new String[2];
  
  // -----------------------------------------------------------------------------
  // ------------------ Start of inherited Plug In functions ---------------------
  // -----------------------------------------------------------------------------

 /**
  * This is called when a data record is encountered. You should do any normal
  * processing here.
  */
  public IRecord procValidRecord(IRecord r)
  {
    CDRRecord CurrentRecord;
    String RegexGroup;
    ArrayList<String> RegexResult;

    CurrentRecord = (CDRRecord) r;
    ChargePacket tmpCP;

    if (CurrentRecord.RECORD_TYPE == CDRRecord.CDR_RECORD)
    {
      // Use only the base tariff looking for bundles
      RegexGroup = "Tariff";

      // Prepare the paramters to perform the search on:
      //  - The user tariff we are using
      //  - The destination of the call
      tmpSearchParameters[0] = CurrentRecord.UserTariff;
      tmpSearchParameters[1] = CurrentRecord.ZoneDestination;

      // Get the discount to apply to this CDR, plus any rate override
      RegexResult = getRegexMatchWithChildData(RegexGroup,tmpSearchParameters);

      // get the results if we had a match
      if (RegexResult != null && (RegexResult.get(0).equalsIgnoreCase("nomatch") == false))
      {
        CurrentRecord.UsedDiscount = RegexResult.get(0);
        CurrentRecord.DiscountOption = RegexGroup;

        // Now see about a rate override
        if (RegexResult.get(1).equalsIgnoreCase("none") == false)
        {
          // Duplicate the charge packet
          tmpCP = CurrentRecord.getChargePacket(0).Clone();

          // and populate it for the rate override
          tmpCP.PriceGroup = RegexResult.get(1);
          tmpCP.PacketType = "O";

          // and add it back
          CurrentRecord.addChargePacket(tmpCP);
        }

        // Get the initial values, we might nees these later if we have to
        // Open a new cycle
        CurrentRecord.DiscountRUM = RegexResult.get(2);
        CurrentRecord.DiscountCounter = Integer.parseInt(RegexResult.get(3));
        CurrentRecord.DiscountInitValue = Double.parseDouble(RegexResult.get(4));
        CurrentRecord.DiscountPeriod = RegexResult.get(5);
      }
    }

    return r;
  }

 /**
  * This is called when a data record with errors is encountered. You should do
  * any processing here that you have to do for error records, e.g. stratistics,
  * special handling, even error correction!
  */
  public IRecord procErrorRecord(IRecord r)
  {
    return r;
  }
}
