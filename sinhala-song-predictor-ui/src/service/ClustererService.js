import axios from "axios";
import {BASE_URL} from "../common/Settings";

class ClustererService {
    predictCluster = async (cluster) => {
        const {data: response} = await axios.get(BASE_URL + '/predict-cluster?cluster='+cluster);
        return response;
    }
}

export default new ClustererService();