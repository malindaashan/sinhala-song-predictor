import axios from "axios";
import {BASE_URL} from "../common/Settings";

class DataSetCreatorService {
    saveAndExtractFeatures = async (algorithm, file) => {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("algorithm", JSON.stringify(algorithm));

        const {data: response} = await axios.post(BASE_URL + '/dataset-creator/save-extract-features', formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                },
            });
        return response;
    }
    getAllPaginatedSavedData = async (page, size) => {
        const {data: response} =
            await axios.get(BASE_URL + '/dataset-creator/find-by-paginated?page=' + page + '&size=' + size);
        return response;
    }

    getTotalRowCount = async () => {
        const {data: response} =
            await axios.get(BASE_URL + '/dataset-creator/count');
        return response;
    }

}

// eslint-disable-next-line import/no-anonymous-default-export
export default new DataSetCreatorService();