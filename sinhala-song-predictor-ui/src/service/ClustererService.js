import axios from "axios";
import {BASE_URL} from "../common/Settings";

class ClustererService {
    predictCluster = async (algorithm) => {
        const {data: response} = await axios.get(BASE_URL + '/cluster/predict-cluster?algorithm='+algorithm);
        return response;
    }
}

export default new ClustererService();