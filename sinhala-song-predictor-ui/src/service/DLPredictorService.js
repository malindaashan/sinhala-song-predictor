import axios from "axios";
import {BASE_URL} from "../common/Settings";

class DLPredictorService {
    getAccuracy = async (algorithm) => {
        const {data: response} = await axios.get(BASE_URL + '/dl/dl-accuracy?algorithm='+algorithm);
        return response;
    }
}

export default new DLPredictorService();