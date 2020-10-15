package me.silloy.file;

import java.util.List;

/**
 * @author shaohuasu
 * @date 2019-03-04 14:47
 * @since 1.8
 */
public class ProcessDataByPostgisListeners extends ReaderFileListener {


    ProcessDataByPostgisListeners(String encode) {
        super();
        super.setEncode(encode);
    }

    @Override
    public void output(List<String> stringList) throws Exception {

    }
}
