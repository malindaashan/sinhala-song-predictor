import axios from "axios";
import {BASE_URL} from "../common/Settings";

class ClustererService {
    predictCluster = async (algorithm, file) => {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("algorithm", algorithm);
        const {data: response} = await axios.post(BASE_URL + '/cluster/predict-cluster', formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                },
            });
        return response;
    }
}

export default new ClustererService();